#!/bin/sh

echo device > /proc/ambarella/usbphy0
modprobe usbcore
modprobe ehci-hcd
modprobe ohci-hcd
modprobe udc-core
modprobe ambarella_udc
modprobe libcomposite
modprobe g_ether
ifconfig usb0 up
modprobe usbnet
modprobe cdc_ether
modprobe rndis_host
modprobe asix
echo "\"ifconfig usb0 your_ip\" after host detects usb ethernet device."

