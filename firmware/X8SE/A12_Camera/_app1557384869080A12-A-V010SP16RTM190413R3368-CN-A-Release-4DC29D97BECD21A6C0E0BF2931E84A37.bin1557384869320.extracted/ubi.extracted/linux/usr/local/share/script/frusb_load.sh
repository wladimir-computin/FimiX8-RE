#!/bin/sh

modprobe usbcore
modprobe ehci-hcd
modprobe ohci-hcd
#modprobe udc-core
#modprobe ambarella_udc
modprobe configfs
modprobe libcomposite
modprobe scsi_mod
modprobe sd_mod
modprobe usb-storage
modprobe usbnet
modprobe cdc_ether
modprobe rndis_host
modprobe asix
