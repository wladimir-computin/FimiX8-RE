#!/bin/sh
echo device > /proc/ambarella/usbphy0
modprobe usbcore
modprobe ehci-hcd
modprobe ohci-hcd
modprobe udc-core
modprobe ambarella_udc
modprobe libcomposite
modprobe g_ether
modprobe usbnet
modprobe cdc_ether
modprobe rndis_host
modprobe asix
brctl addbr br0
brctl addif br0 usb0
brctl addif br0 eth0
ifconfig br0 192.168.8.100
ifconfig eth0 up
