#!/bin/sh

if [ "${1}" == "fast" ]; then
	hciconfig hci0 noscan
	hciconfig hci0 noleadv
	exit 0
fi

killall hcitool bluetoothd bt-agent hciconfig gatttool btgatt-server 2>/dev/null
checkfuse=`cat /proc/mounts | grep /tmp/fuse_d`
if [ "${checkfuse}" == "" ]; then
	fuse_d="/tmp/fuse_d"
else
	fuse_d="/tmp/fuse_d"
fi

hciconfig hci0 down 2>/dev/null
killall hciattach brcm_patchram_plus bluetoothctl 2>/dev/null
bt_conf=`cat /pref/bt.conf | grep -Ev "^#"`
export `echo "${bt_conf}"|grep -vI $'^\xEF\xBB\xBF'`
hci_on=`hciconfig`
if [ "${BT_EN_GPIO}" != "" ] && [ "${hci_on}" == "" ]; then
	if [ "${BT_EN_STATUS}" == "" ]; then
		BT_EN_STATUS=1
	fi
	if [ ! -e /sys/module/sd8xxx ]; then
		/usr/local/share/script/t_gpio.sh ${BT_EN_GPIO} $(($(($BT_EN_STATUS + 1)) % 2))
	fi
fi

if [ "${1}" != "" ]; then
	mounted=`cat /proc/mounts | grep /tmp/lib/bluetooth`
	if [ "${mounted}" == "" ]; then
		#reset database
		rm -rf /tmp/lib/bluetooth/
	else
		#in case vffs hang
		rm -f `find ${fuse_d}/MISC/bluetooth/ -type f`
		rm -rf ${fuse_d}/MISC/bluetooth/*
	fi
fi
if [ -e /sys/module/rtk_btusb ]; then
	rmmod rtk_btusb
fi
if [ -e /sys/module/bt8xxx ]; then
	rmmod bt8xxx
fi
