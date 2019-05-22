#!/bin/sh

## call this function after waken up
if [ "${1}" == "resume" ]; then
	if [ -e /sys/module/8189es ]; then
		MODE=`iw wlan0 info | grep type  | awk '{print $2}'`
		if [ "${MODE}" == "AP" ] || [ "${MODE}" == "P2P-GO" ]; then
			# AP, P2P-GO
			iwpriv wlan0 ap_wow_mode disable
		else
			# Stationi, P2P-Client  mode wow
			iwpriv wlan0 wow_mode disable
		fi
	elif [ -e /sys/module/8723bs ]; then
		# Station mode wow
		iwpriv wlan0 wow_mode disable
	fi

	brcm_bt=`ps|grep brcm_patchram_plus|grep -v grep`
	if [ "${brcm_bt}" != "" ]; then
		waked_by_ibeancon=`ps | grep "hcitool lescan \-\-duplicate" | grep -v grep`
		if [ "${waked_by_ibeancon}" != "" ]; then
			killall -2 hcitool
			#turn off adv filter
			hcitool cmd 0x3F 0xE9 01 00 00 00 00
		fi
	fi

	exit 0
fi

if [ -e /sys/module/bcmdhd ]; then
	#filter index=200, byte36=0x1E, byte37=0xC5; udp.dstport == 7877
	wl pkt_filter_add 200 0 0 36 0xffff 0x1EC5
	wl pkt_filter_enable 200 1
elif [ -e /sys/kernel/debug/ieee80211/phy0/ath6kl/wow_pattern ]; then
	#filter index=0, byte44=0x1E, byte45=0xC5; udp.dstport == 7877
	iw wlan0 wowlan enable disconnect
	echo -en '\x1E\xC5' > /tmp/match.bin
	echo 0 44 /tmp/match.bin > /sys/kernel/debug/ieee80211/phy0/ath6kl/wow_pattern
elif [ -e /sys/module/ar6000 ]; then
	wmiconfig -i wlan0 --sethostmode asleep
	wmiconfig -i wlan0 --setwowmode enable
	# Edit here to Customize Magic Packet filter
	echo "This example uses ip.protocol=UDP AND UDP.dst_port=7877 as wakeup packet"
	wmiconfig -i wlan0 --addwowpattern 0 15 31 110000000000000000000000001EC5 FF000000000000000000000000FFFF
	# wait 1 sec to take effect
	sleep 1
elif [ -e /sys/module/8189es ]; then
	MODE=`iw wlan0 info | grep type  | awk '{print $2}'`
	if [ "${MODE}" == "AP" ] || [ "${MODE}" == "P2P-GO" ]; then
		# AP, P2P-GO
		iwpriv wlan0 ap_wow_mode enable
	else
		# Stationi, P2P-Client  mode wow
		iwpriv wlan0 wow_mode enable
	fi
elif [ -e /sys/module/8723bs ]; then
	# Station mode wow
	iwpriv wlan0 wow_mode enable
elif [ -e /sys/module/bt8xxx ]; then
	#use GPIO#4 and gap=0x50
	echo "gpio_gap=0x0450" > /proc/mbt/hci0/config
	#host sleep settings
	echo "hscfgcmd=1" > /proc/mbt/hci0/config
	echo "hsmode=1" > /proc/mbt/hci0/config
	echo "hscmd=1" > /proc/mbt/hci0/config
elif [ -e /sys/module/sd8xxx ]; then
	#2=unicast event, GPIO#1, gap:0x50=80ms
	#Condition:
	# bit 0 = 1   -- broadcast data
	# bit 1 = 1   -- unicast data
	# bit 2 = 1   -- mac event
	# bit 3 = 1   -- multicast data
	# bit 6 = 1  --  Wakeup when mgmt frame received.
	# bit 7 = 1  --  Reserved
	# bit 8 = 1  --  Disable non maskable data wakeup.
	/usr/local/mrvl/mlanutl wlan0 hscfg 2 1 0x50
	#iwpriv wlan0 hscfg 2 1 0x50
fi

brcm_bt=`ps|grep brcm_patchram_plus|grep -v grep`
if [ "${brcm_bt}" != "" ]; then
	echo "apply workaround for hci tx timeout err: wakeup BT before suspend, let hardware control btwake during suspend"
	echo 0 > /proc/bluetooth/sleep/btwake

	## echo "44:33:22:CC:BB:AA" > /tmp/BT_MAC
	## unmark following for Wakeup-On-iBeacon filter
#	# disconnect & clear whitelist
#	killall hcitool gatttool
#	hcitool lewlclr
#
#	# apply filter: adv type "0xff" data prefix "4c 00 02"
#	filter="4c 00 02"
#	hcitool cmd 0x3F 0xE9 0x0A 0x00 ${filter}
#
#	# match MAC address
#	RMAC=`cat /tmp/BT_MAC|awk -F ":" '{print $6, $5, $4, $3, $2, $1}'`
#	hcitool cmd 0x3F 0xE9 0x06 00 ${RMAC} 00
#	hcitool cmd 0x3F 0xE9 05 00 00 00 00 00 00 00 02 21 00 00 00 01
#
#	#enable adv filter
#	hcitool cmd 0x3F 0xE9 01 02 00 00 00
#	#start scan
#	hcitool lescan --duplicate &

	## NOTE: To wakeup your camera, start iBEACON in your remote controller by:
	#hciconfig hci0 leadv
	#hcitool cmd 0x08 0x0008 1e 02 01 1e 1a ff 4c 00 02 15 e2 c5 6d b5 df fb 48 d2 b0 60 d0 f5 a7 10 96 e0 00 00 00 00 c5 00
fi

# disable iscan to save BT power
#which bluetoothctl && echo 'discoverable off'| bluetoothctl

# drop cache
echo 3 > /proc/sys/vm/drop_caches

# enter self refresh mode
if [ "${1}" == "" ]; then
	echo sr > /sys/power/state
fi
