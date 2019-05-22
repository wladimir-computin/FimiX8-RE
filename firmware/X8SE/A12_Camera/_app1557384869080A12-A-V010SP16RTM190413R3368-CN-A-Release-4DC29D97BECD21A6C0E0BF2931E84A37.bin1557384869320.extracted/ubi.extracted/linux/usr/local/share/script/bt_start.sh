#!/bin/sh

# use nice to prevent data loss
CMD_NICER ()
{
	echo "$@"
	nice -n -20 $@
}

CMD_LOGGER ()
{
	echo "$@"
	$@
}

#Do not save conf to SD card
#SYNC_CONIG ()
#{
#	checkfuse=`cat /proc/mounts | grep /tmp/fuse_d`
#	if [ "${checkfuse}" == "" ]; then
#		fuse_d="/tmp/fuse_d"
#	else
#		fuse_d="/tmp/fuse_d"
#	fi
#	if [ -e /tmp/bt.conf ]; then
#		echo "==> Load bt.conf from /tmp ..."
#		btconf=`cat /tmp/bt.conf | sed -e 's/\r$//'`
#		echo "${btconf}" > /pref/bt.conf
#	elif [ ! -e /pref/bt.conf ]; then
#		mkdir -p ${fuse_d}/MISC
#		cp /usr/local/share/script/bt.conf /pref/bt.conf
#	fi
#}

SYNC_CONIG ()
{
	checkfuse=`cat /proc/mounts | grep /tmp/fuse_d`
	if [ "${checkfuse}" == "" ]; then
		fuse_d="/tmp/fuse_d"
	else
		fuse_d="/tmp/fuse_d"
	fi

	#tmp -> pref, misc
	if [ -e /tmp/bt.conf ]; then
		echo "==> Load bt.conf from /tmp ..."
		btconf=`cat /tmp/bt.conf | sed -e 's/\r$//'`
		echo "${btconf}" > /pref/bt.conf
		btconf=`cat /pref/bt.conf | sed -e 's/$/\r/'`
		echo "${btconf}" > ${fuse_d}/MISC/bt.conf
	#misc -> pref
	elif [ -e ${fuse_d}/MISC/bt.conf ]; then
		echo "==> Load bt.conf from SD/MISC..."
		btconf=`cat ${fuse_d}/MISC/bt.conf | sed -e 's/\r$//'`
		echo "${btconf}" > /pref/bt.conf
	#pref -> misc
	elif [ -e /pref/bt.conf ]; then
		mkdir -p ${fuse_d}/MISC
		btconf=`cat /pref/bt.conf | sed -e 's/$/\r/'`
		echo "${btconf}" > ${fuse_d}/MISC/bt.conf
	#fw -> pref, misc
	else
		cp /usr/local/share/script/bt.conf /pref/bt.conf
		mkdir -p ${fuse_d}/MISC
		btconf=`cat /pref/bt.conf | sed -e 's/$/\r/'`
		echo "${btconf}" > ${fuse_d}/MISC/bt.conf
	fi
}

reset_conf ()
{
	echo "reset bt.conf"
	cp /usr/local/share/script/bt.conf /pref/bt.conf
	btconf=`cat /pref/bt.conf | sed -e 's/$/\r/'`
	echo "${btconf}" > ${fuse_d}/MISC/bt.conf
}

reset_bluez ()
{
	killall bluetoothd bluetoothctl 2>/dev/null
	echo "reset bluetoothd"
	rm -f /pref/bluetooth/* ${fuse_d}/MISC/bluetooth/*
	START_BLUEZ
}

START_BLUEZ ()
{
	if [ "${bluez_5}" == "1" ]; then
		sed -i -e 's|\(.*\)PairableTimeout =\(.*\)|PairableTimeout = '${BT_PAIRABLE_TIMEOUT}'|g' /tmp/bluetooth/main.conf
		sed -i -e 's|\(.*\)DiscoverableTimeout =\(.*\)|DiscoverableTimeout = '${BT_DISCOVERABLE_TIMEOUT}'|g' /tmp/bluetooth/main.conf

		if [ "${GATT_PERIPHERAL}" == "yes" ] && [ "${bluez_5_29_higher}" == "" ]; then
			#bluez less or equal 5.28: use gatt-example plugin
			CMD_LOGGER /usr/libexec/bluetooth/bluetoothd -C -a
		else
			#bluez greater or equal 5.29: gatt-example plugin do not work
			CMD_LOGGER /usr/libexec/bluetooth/bluetoothd -C
		fi
	else
		if [ "${GATT_PERIPHERAL}" == "yes" ]; then
			CMD_LOGGER bluetoothd -a
		else
			CMD_LOGGER bluetoothd
		fi
	fi
}

wait_hci0 ()
{
	n=0
	while [ ! -e /sys/class/bluetooth/hci0/address ] && [ $n -ne 30 ]; do
		n=$(($n + 1))
		CMD_LOGGER sleep 0.1
	done
	if [ ! -e /sys/class/bluetooth/hci0/address ]; then
		echo "There is no BT interface!"
		#exit 1
	fi

	n=0
	wifi1_mac=`hciconfig -a | grep "BD Addr"|awk '{print $3}'`
	while [ "${wifi1_mac}" == "" ] && [ $n -ne 30 ]; do
		n=$(($n + 1))
		CMD_LOGGER sleep 0.1
		wifi1_mac=`hciconfig -a | grep "BD Addr"|awk '{print $3}'`
	done

	n=0
	while [ "${wifi1_mac}" == "00:00:00:00:00:00" ] && [ $n -ne 30 ]; do
		n=$(($n + 1))
		CMD_LOGGER sleep 0.1
		wifi1_mac=`hciconfig -a | grep "BD Addr"|awk '{print $3}'`
	done

	if [ "${wifi1_mac}" == "" ] || [ "${wifi1_mac}" == "00:00:00:00:00:00" ]; then
		echo "There is no BT interface!"
		reset_conf
		exit 1
	fi
}

mmc_remove ()
{
	if [ "${WIFI_EN_STATUS}" == "" ]; then
		WIFI_EN_STATUS=1
	fi

	mmci=`grep mmc /proc/ambarella/mmc_fixed_cd |awk $'{print $1}'|cut -c 4|tail -n 1`
	echo "${mmci} 0" > /proc/ambarella/mmc_fixed_cd

	/usr/local/share/script/t_gpio.sh ${WIFI_EN_GPIO} $(($(($WIFI_EN_STATUS + 1)) % 2))
}

wait_mmc_add ()
{
	mmci=`grep mmc /proc/ambarella/mmc_fixed_cd |awk $'{print $1}'|cut -c 4|tail -n 1`
	echo "${mmci} 1" > /proc/ambarella/mmc_fixed_cd

	n=0
	while [ "`ls /sys/bus/sdio/devices`" == "" ] && [ $n -ne 30 ]; do
		n=$(($n + 1))
		sleep 0.1
	done
}

HCI0_BRINGUP ()
{
	if [ "${UART_NODE}" == "" ]; then
		reset_conf
		return 1
	fi

	UART_BUSY=`grep ${UART_NODE##/*/} /etc/inittab`
	if [ "${UART_BUSY}" != "" ]; then
		echo "Wrong bluetooth UART config in /etc/inittab: ${UART_BUSY}"
		exit 1
	fi

	if [ -e /proc/ambarella/uart1_rcvr ]; then
		#echo "set UART1 to 1 byte FIFO threshold"
		echo 0 > /proc/ambarella/uart1_rcvr
	fi

	if [ "${BT_EN_STATUS}" == "" ]; then
		BT_EN_STATUS=1
	fi
	if [ "${BT_EN_GPIO}" != "" ] && [ ! -e /sys/module/sd8xxx ]; then
		/usr/local/share/script/t_gpio.sh ${BT_EN_GPIO} $(($(($BT_EN_STATUS + 1)) % 2))
		/usr/local/share/script/t_gpio.sh ${BT_EN_GPIO} ${BT_EN_STATUS}
		CMD_LOGGER sleep 0.1
	fi

	if [ "${HCI_DRIVER}" == "mrvl" ] && [ "`ls /sys/bus/sdio/devices`" == "" ]; then
		wait_mmc_add
	fi
	if [ "${UART_BAUD}" == "" ]; then
		UART_BAUD=750000
	fi

	if [ "${HCI_DRIVER}" == "brcm" ]; then
		if [ -e /tmp/wifi1_mac ]; then
			CMD_NICER brcm_patchram_plus --enable_hci --baudrate ${UART_BAUD} --use_baudrate_for_download --patchram /usr/local/bcmdhd/bt.hcd --no2bytes --enable_lpm ${UART_NODE} --bd_addr `cat /tmp/wifi1_mac`
		else
			CMD_NICER brcm_patchram_plus --enable_hci --baudrate ${UART_BAUD} --use_baudrate_for_download --patchram /usr/local/bcmdhd/bt.hcd --no2bytes --enable_lpm ${UART_NODE}
		fi

		if [ "${1}" == "start" ]; then
			wifi_start=`grep wifi_start.sh /etc/init.d/S52wifi`
			if [ -e ${fuse_d}/MISC/wifi.conf ]; then
				wifi_conf=`cat ${fuse_d}/MISC/wifi.conf | grep -Ev "^#" | sed -e 's/\r$//'`
			else
				wifi_conf=`cat /pref/wifi.conf | grep -Ev "^#"`
			fi
			export `echo "${wifi_conf}"|grep -v PASSW|grep -v SSID|grep -vI $'^\xEF\xBB\xBF'`

			if [ "${wifi_start}" == "" ] && [ "${WIFI_SWITCH_GPIO}" == "" ] && [ "${WIFI_EN_GPIO}" != "" ]; then
				mmc_remove
			fi
		fi
		#enable bluesleep dynamic power saving
		echo 1 > /proc/bluetooth/sleep/proto
	elif [ "${HCI_DRIVER}" == "rtk_btusb" ]; then
		if [ -e /sys/module/rtk_btusb/parameters/bd_addr ]; then
			if [ -e /tmp/wifi1_mac ]; then
				cat /tmp/wifi1_mac > /sys/module/rtk_btusb/parameters/bd_addr
			fi
		else
			if [ -e /tmp/wifi1_mac ]; then
				insmod /usr/local/*/rtk_btusb.ko bd_addr=`cat /tmp/wifi1_mac`

			else
				insmod /usr/local/*/rtk_btusb.ko
			fi
		fi
		rtl8723bu_workaround=`lsmod|grep ehci`
		if [ "${rtl8723bu_workaround}" == "" ]; then
			modprobe ehci_hcd 2> /dev/null
			echo "host" > /proc/ambarella/usbphy0
			echo -e "\e[1;31m Apply workaround for rtl8723bu iOS cannot connect BLE: sleep 4\n\n \e[0m"
			sleep 4
		fi
	elif [ "${HCI_DRIVER}" == "mrvl" ]; then
		if [ -e /tmp/wifi1_mac ]; then
			insmod /usr/local/mrvl/bt8xxx.ko drv_mode=1 psmode=1 cal_cfg=mrvl/bt_cal_data.conf bt_mac=`cat /tmp/wifi1_mac`
		else
			insmod /usr/local/mrvl/bt8xxx.ko drv_mode=1 psmode=1 cal_cfg=mrvl/bt_cal_data.conf
		fi
	else
		if [ -e /tmp/wifi1_mac ]; then
			CMD_NICER hciattach ${UART_NODE} ${HCI_DRIVER} ${UART_BAUD} flow sleep -d -m /tmp/wifi1_mac
		else
			CMD_NICER hciattach ${UART_NODE} ${HCI_DRIVER} ${UART_BAUD} flow sleep -d
		fi
	fi

	wait_hci0

	#save /tmp/lib/bluetooth
	mounted=`cat /proc/mounts | grep /tmp/lib/bluetooth`
	if [ "${mounted}" == "" ]; then
		mkdir -p /tmp/lib/bluetooth/${wifi1_mac}
		mkdir -p ${fuse_d}/MISC/bluetooth
		if [ -e ${fuse_d}/MISC/bluetooth ]; then
			echo "skip mount --bind ${fuse_d}/MISC/bluetooth/ /tmp/lib/bluetooth/${wifi1_mac}"
			#mount --bind ${fuse_d}/MISC/bluetooth/ /tmp/lib/bluetooth/${wifi1_mac}
		fi
	fi

	START_BLUEZ
#	CMD_NICER hciconfig hci0 up
	echo 'power on' | bluetoothctl
}

DO_BT_AGENT ()
{
	if [ "${bluez_5}" == "1" ]; then
		if [ "${BT_LEGACY_PIN}" == "" ]; then
			CMD_LOGGER bluetoothctl -d -p 0000 -a NoInputNoOutput
		else
			CMD_LOGGER bluetoothctl -d -p ${BT_LEGACY_PIN} -a NoInputNoOutput
		fi
	else
		if [ "${BT_LEGACY_PIN}" == "" ]; then
			CMD_LOGGER bt-agent -d --auto_pk -s NoInputNoOutput
		else
			echo -e "\033[032m Bluetooth PIN code=${BT_LEGACY_PIN} \033[0m"
			CMD_LOGGER bt-agent -d --auto_pk -p ${BT_LEGACY_PIN}
		fi
		#CMD_LOGGER bt-monitor_headset -s /usr/local/share/script/bt_speakergain.sh -d
	fi
}

SET_PISCAN ()
{
	if [ "${bluez_5}" == "1" ]; then
		if [ "${ISCAN}" == "yes" ]; then
			echo 'discoverable on'| bluetoothctl
		else
			echo 'discoverable off'| bluetoothctl
		fi
		if [ "${PSCAN}" == "yes" ]; then
			echo 'pairable on'| bluetoothctl
		else
			echo 'pairable off'| bluetoothctl
		fi
	else
		if [ "${ISCAN}" == "yes" ] && [ "${PSCAN}" == "yes" ]; then
			CMD_NICER hciconfig hci0 piscan
		elif [ "${ISCAN}" == "yes" ]; then
			CMD_NICER hciconfig hci0 iscan
		elif [ "${PSCAN}" == "yes" ]; then
			CMD_NICER hciconfig hci0 pscan
		else
			CMD_NICER hciconfig hci0 noscan
		fi
		if [ "${BT_DISCOVERABLE_TIMEOUT}" != "0" ]; then
			CMD_LOGGER bt-adapter --set DiscoverableTimeout ${BT_DISCOVERABLE_TIMEOUT}
		fi
		if [ "${BT_PAIRABLE_TIMEOUT}" != "0" ]; then
			CMD_LOGGER bt-adapter --set PairableTimeout ${BT_PAIRABLE_TIMEOUT}
		fi
	fi
}

##### main ##########################################

if [ "${1}" == "fast" ]; then
	hciconfig hci0 pscan
	hciconfig hci0 leadv
	exit 0
fi

SYNC_CONIG

bt_conf=`cat /pref/bt.conf | grep -Ev "^#"`
export `echo "${bt_conf}"|grep -vI $'^\xEF\xBB\xBF'`
which bluetoothctl && bluez_5=1 && which btgatt-server && bluez_5_29_higher=1

if [ "${DEVICE_NAME}" != "" ]; then
	sed -i -e 's|\(.*\)Name =\(.*\)|Name = '${DEVICE_NAME}'|g' /tmp/bluetooth/main.conf
	if [ -e ${fuse_d}/MISC/bluetooth/config ]; then
		btconfig=`cat ${fuse_d}/MISC/bluetooth/config | sed -e 's|^name \(.*\)|name '${DEVICE_NAME}'|g'`
		echo "${btconfig}" > ${fuse_d}/MISC/bluetooth/config
	fi
	if [ -e /pref/bluetooth/config ]; then
		btconfig=`cat /pref/bluetooth/config | sed -e 's|^name \(.*\)|name '${DEVICE_NAME}'|g'`
		echo "${btconfig}" > /pref/bluetooth/config
	fi
else
	DEVICE_NAME="amba"
fi

if [ ! -e /sys/class/bluetooth/hci0 ]; then
	HCI0_BRINGUP ${@}
else
	START_BLUEZ
	CMD_NICER hciconfig hci0 up
fi


SET_PISCAN

DO_BT_AGENT

#in case bluetoothd is dead
tmp=`ps|grep -v grep|grep bluetoothd`
if [ "${tmp}" == "" ]; then
	reset_bluez
	DO_BT_AGENT
fi

if [ "${GATT_PERIPHERAL}" == "yes" ] && [ "${bluez_5_29_higher}" == "1" ]; then
	CMD_LOGGER btgatt-server -n ${DEVICE_NAME}
fi

#wait [CHG] Controller xx:xx:xx:xx:xx:xx Powered: yes
if [ "${BT_LEGACY_PIN}" == "" ]; then
	CMD_NICER hciconfig hci0 sspmode 1
else
	CMD_NICER hciconfig hci0 sspmode 0
fi
