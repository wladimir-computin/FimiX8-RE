OpenWRT Firmware for the Remote Control.

Binwalk can unpack some parts of it, but the jiffs2 overlay is missing.
See: https://openwrt.org/docs/techref/flash.layout

Very similar to FC-Relay, shares most of the code

Interesting files:

* extracted/squashfs-root/sbin/fmupdate -- We need to understand how the update works!
* extracted/squashfs-root/sbin/fmlink-drone
* extracted/squashfs-root/sbin/fmlink-gs

I zipped the binwalk output, just unzip it if you want to take look

We have the squashfs root, but most of the interesting stuff seems to be in the JFFS2 overlay.
