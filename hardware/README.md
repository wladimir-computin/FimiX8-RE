## Connection of Drone to USB
* without battery:
```
	[166107.020056] usb 1-2: new high-speed USB device number 4 using xhci_hcd
	[166107.168210] usb 1-2: New USB device found, idVendor=1a40, idProduct=0801, bcdDevice= 1.00
	[166107.168212] usb 1-2: New USB device strings: Mfr=0, Product=1, SerialNumber=0
	[166107.168213] usb 1-2: Product: USB 2.0 Hub
	[166107.168908] hub 1-2:1.0: USB hub found
	[166107.168926] hub 1-2:1.0: 4 ports detected
```
* with battery:
```
	[169039.499095] usb 1-2: new high-speed USB device number 12 using xhci_hcd
	[169039.647536] usb 1-2: New USB device found, idVendor=1a40, idProduct=0801, bcdDevice= 1.00
	[169039.647542] usb 1-2: New USB device strings: Mfr=0, Product=1, SerialNumber=0
	[169039.647545] usb 1-2: Product: USB 2.0 Hub
	[169039.648605] hub 1-2:1.0: USB hub found
	[169039.648655] hub 1-2:1.0: 4 ports detected
	[169039.935218] usb 1-2.2: new high-speed USB device number 13 using xhci_hcd
	[169040.036657] usb 1-2.2: config 1 interface 1 altsetting 0 bulk endpoint 0x82 has invalid maxpacket 256
	[169040.037814] usb 1-2.2: New USB device found, idVendor=4262, idProduct=0062, bcdDevice= 0.01
	[169040.037820] usb 1-2.2: New USB device strings: Mfr=1, Product=2, SerialNumber=3
	[169040.037824] usb 1-2.2: Product: Fimi cdcmsc class
	[169040.037826] usb 1-2.2: Manufacturer: Fimi
	[169040.037829] usb 1-2.2: SerialNumber: 0001
	[169040.043406] cdc_acm 1-2.2:1.0: ttyACM0: USB ACM device
	[169040.045644] usb-storage 1-2.2:1.2: USB Mass Storage device detected
	[169040.046424] scsi host0: usb-storage 1-2.2:1.2
	[169040.127097] usb 1-2.3: new full-speed USB device number 14 using xhci_hcd
	[169040.248473] usb 1-2.3: New USB device found, idVendor=0483, idProduct=572a, bcdDevice= 2.00
	[169040.248476] usb 1-2.3: New USB device strings: Mfr=1, Product=2, SerialNumber=3
	[169040.248478] usb 1-2.3: Product: FIMI Mass Storage
	[169040.248479] usb 1-2.3: Manufacturer: FIMI FC
	[169040.248481] usb 1-2.3: SerialNumber: 00000000001A
	[169040.255912] usb-storage 1-2.3:1.0: USB Mass Storage device detected
	[169040.256345] scsi host1: usb-storage 1-2.3:1.0
	[169041.052704] scsi 0:0:0:0: Direct-Access     FROBOT   X8S Platform     1000 PQ: 0 ANSI: 0
	[169041.053441] sd 0:0:0:0: Attached scsi generic sg0 type 0
	[169041.053980] sd 0:0:0:0: [sda] 250085376 512-byte logical blocks: (128 GB/119 GiB)
	[169041.054659] sd 0:0:0:0: [sda] Write Protect is off
	[169041.054663] sd 0:0:0:0: [sda] Mode Sense: 0c 00 00 08
	[169041.058066] sd 0:0:0:0: [sda] No Caching mode page found
	[169041.058075] sd 0:0:0:0: [sda] Assuming drive cache: write through
	[169041.065847]  sda: sda1
	[169041.068033] sd 0:0:0:0: [sda] Attached SCSI removable disk
	[169041.276087] scsi 1:0:0:0: Direct-Access     FIMI     FC-BLACKBOX      0.01 PQ: 0 ANSI: 2
	[169041.276512] scsi 1:0:0:1: Direct-Access     FIMI     FC-UPGRADE       0.01 PQ: 0 ANSI: 2
	[169041.277287] sd 1:0:0:0: Attached scsi generic sg1 type 0
	[169041.277594] sd 1:0:0:0: [sdb] 236031 512-byte logical blocks: (121 MB/115 MiB)
	[169041.277861] sd 1:0:0:0: [sdb] Write Protect is off
	[169041.277866] sd 1:0:0:0: [sdb] Mode Sense: 00 00 00 00
	[169041.277965] sd 1:0:0:1: Attached scsi generic sg2 type 0
	[169041.278410] sd 1:0:0:0: [sdb] Asking for cache data failed
	[169041.278418] sd 1:0:0:0: [sdb] Assuming drive cache: write through
	[169041.278755] sd 1:0:0:1: [sdc] 6655 512-byte logical blocks: (3.41 MB/3.25 MiB)
	[169041.279340] sd 1:0:0:1: [sdc] Write Protect is off
	[169041.279346] sd 1:0:0:1: [sdc] Mode Sense: 00 00 00 00
	[169041.279760] sd 1:0:0:1: [sdc] Asking for cache data failed
	[169041.279770] sd 1:0:0:1: [sdc] Assuming drive cache: write through
	[169041.288622]  sdb: sdb1
	[169041.295368]  sdc: sdc1
	[169041.296313] sd 1:0:0:0: [sdb] Attached SCSI removable disk
	[169041.297717] sd 1:0:0:1: [sdc] Attached SCSI removable disk
```
