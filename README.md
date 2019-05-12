
# FimiX8-RE
Research on hacking our Fimi X8 SE

RC and Drone are powered by OpenWRT.

Tools used so far:
* jadx 0.9
* mitmproxy
* binwalk
* Ghidra

Didn't had much time to document everything yet. Look for files named README.md, I write down my notes in those.

## Stuff we know
* The 5km range is achieved by some custom 5GHz wifi implementation between the RC and the drone. Connection to the phone is over USB. (That's actually a pretty good solution, using wifi of the phone results always in low range and high latency)
** Further analysis indicates, that iw/iwconfig is used for tx power management.
* Firmware of the drone and RC consists of different files for different parts of the hardware.
  * Drone: FC, FC-Relay, ESC, NFZ, Gimbal, Camera, 
  * RC: RC, RC-Relay
  * FC, RC, ESC, NFZ, Gimbal are packed or encrypted.
  * FC-Relay, RC-Relay and Camera are packed but it seems not too hard to unpack them. Binwalk manages to unpack some parts of the firmware. Thats an entry point, we should try to unpack the complete image.
  * fmupdate (from unpacked RC/FC-Relay) seems to handle firmware unpacking. We need to understand how it exactly works. HELP NEEDED HERE!
* The Android app Fimi Navi is little bit bloated but not obfuscated and extracting the X8SDK is pretty straightforward. Can do that later.

## Contribution
Help is desperately needed!
Anyone with skills in Reverse Engineering, OpenWRT, Embedded, MIPS and so on.
ATM I'm trying to unpack the second stage firmware image (jiffs2 overlay).
