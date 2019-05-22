
# Firmware stuff

* /X8SE should be our X8 SE
* /GH2 contains firmware for the Fimi smartphone gimbal 
* /x9 contains firmware for the Mi Drone Mini.
* /x6_devel contains development firmwares for Mi4K or early X8?
* /rest? Maybe A3? Don't know

~~TODO: Sort them properly!~~ Done!

Unpack firmware with binwalk:
``binwalk -e X8SE/*``

Note: Not every image can be unpacked by binwalk, more research required.
