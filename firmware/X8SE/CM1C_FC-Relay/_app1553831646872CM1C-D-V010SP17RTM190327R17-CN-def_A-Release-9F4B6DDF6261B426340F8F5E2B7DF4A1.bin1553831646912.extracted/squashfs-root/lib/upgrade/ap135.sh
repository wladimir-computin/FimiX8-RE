# copied from allnet.sh

# make sure we got uboot-envtools and fw_env.config copied over to the ramfs
RAMFS_COPY_BIN="/usr/sbin/fw_printenv /usr/sbin/nandwrite /usr/sbin/fw_setenv"
RAMFS_COPY_DATA="/etc/fw_env.config"

# determine size of the main firmware partition
platform_get_firmware_size() {
	local dev size erasesize name
	while read dev size erasesize name; do
		name=${name#'"'}; name=${name%'"'}
		case "$name" in
			firmware)
				printf "%d" "0x$size"
				break
			;;
		esac
	done < /proc/mtd
}

# get the first 4 bytes (magic) of a given file starting at offset in hex format
get_magic_long_at() {
	dd if="$1" skip=$(( $CI_BLKSZ / 4 * $2 )) bs=4 count=1 2>/dev/null | hexdump -v -n 4 -e '1/1 "%02x"'
}

get_filesize() {
	wc -c "$1" | while read image_size _n ; do echo $image_size ; break; done
}

# scan through the update image pages until matching a magic
platform_get_offset() {
	offsetcount=0
	magiclong="x"
	if [ -n "$3" ]; then
		offsetcount=$3
	fi
	while magiclong=$( get_magic_long_at "$1" "$offsetcount" ) && [ -n "$magiclong" ]; do
		case "$magiclong" in
			"2705"*)
				# U-Boot image magic
				if [ "$2" = "uImage" ]; then
					echo $offsetcount
					return
				fi
			;;
			"68737173"|"73717368")
				# SquashFS
				if [ "$2" = "rootfs" ]; then
					echo $offsetcount
					return
				fi
			;;
			"deadc0de"|"19852003")
				# JFFS2 empty page
				if [ "$2" = "rootfs-data" ]; then
					echo $offsetcount
					return
				fi
			;;
			"1985"*)
				# JFFS2 magic
				if [ "$2" = "jffs-rootfs" ]; then
					echo $offsetcount
					return
				fi
			;;

		esac
		offsetcount=$(( $offsetcount + 1 ))
	done
}

platform_check_image_ap135() {
	case "$(ar71xx_board_name)" in
	"ap147" |\
	"ap151" |\
	"ap152" |\
	"ap135" |\
	"ap135-dual")
		local image_size=$( get_filesize "$1" )
		local firmware_size=$( platform_get_firmware_size )
		[ $image_size -ge $firmware_size ] &&
		{
			echo "upgrade image is too big (${image_size}b > ${firmware_size}b)"
		}

		local rootfs_blockoffset=$( platform_get_offset "$1" rootfs )
		[ -z $rootfs_blockoffset ] && {
			echo "missing rootfs"
			return 1
		}
		;;
	"ap135-nand")
		local rootfs_blockoffset=$( platform_get_offset "$1" jffs-rootfs )
		[ -z $rootfs_blockoffset ] && {
			echo "missing jffs-rootfs"
			return 1
		}
		;;
	*)
		echo "Unsupported board type!"
		return 1
		;;
	esac

	local data_blockoffset=$( platform_get_offset "$1" rootfs-data "$rootfs_blockoffset" )
	[ -z $data_blockoffset ] && {
		echo "rootfs doesn't have JFFS2 end marker"
		return 1
	}

	local vmlinux_blockoffset=$( platform_get_offset "$1" uImage "$data_blockoffset" )
	[ -z $vmlinux_blockoffset ] && {
		echo "vmlinux-uImage not found"
		return 1
	}

	return 0
}

platform_do_upgrade_ap135() {
	case "$(ar71xx_board_name)" in
	"ap147" |\
	"ap151" |\
	"ap152" |\
	"ap135" |\
	"ap135-dual")
		local firmware_base_addr=$( printf "%d" "0x9f050000" )
		local vmlinux_blockoffset=$( platform_get_offset "$1" uImage )
		if [ ! -n "$vmlinux_blockoffset" ]; then
			echo "can't determine new kernel address"
			return 1
		fi
		local vmlinux_offset=$(( $vmlinux_blockoffset * $CI_BLKSZ ))
		local vmlinux_addr=$(( $firmware_base_addr + $vmlinux_offset ))
		local vmlinux_hexaddr=0x$( printf "%08x" "$vmlinux_addr" )

		local curr_linux_addr=$(fw_printenv bootcmd | sed 's/.*0x/0x/g')
		[ $(printf "0x%x\n" $curr_linux_addr) != $vmlinux_hexaddr ] && {
			local fw_printenv=/usr/sbin/fw_printenv
			[ ! -x "$fw_printenv" ] && {
				echo "Please install uboot-envtools, aborting!"
				return 1
			}

			[ ! -r "/etc/fw_env.config" ] && {
				echo "/etc/fw_env.config is missing, aborting!"
				return 1
			}

			echo "Updating boot command to: bootm $vmlinux_hexaddr"
			fw_setenv bootcmd "bootm $vmlinux_hexaddr" || {
				echo "failed to	update U-Boot environment, aborting!"
				return 1
			}
		}
		default_do_upgrade "$@"
		;;
	"ap135-nand")
		local sysupgrade_f=$1
		local vmlinux_blockoffset=$( platform_get_offset "$1" uImage )

		mtd erase mtd4
		dd if=$sysupgrade_f bs=$CI_BLKSZ skip=$vmlinux_blockoffset | \
			nandwrite -p /dev/mtd4 -

		mtd erase mtd5
		dd if=$sysupgrade_f bs=$CI_BLKSZ count=$vmlinux_blockoffset | \
			nandwrite -p /dev/mtd5 -
		;;
	*)
		echo "Unsupported board type!"
		return 1
		;;
	esac
}
