
# Firmware stuff

/GH2 contains firmware for the Fimi smartphone gimbal 
/x9 contains firmware for the Mi Drone Mini.
/rtm should be our X8 SE
/rest? Maybe A3? Don't know

TODO: Sort them properly!

The APK was on the server too, seems like an old version of Fimi Navi?

Unpack firmware with binwalk:
``binwalk -e rtm/*``

Note: Not every image can be unpacked by binwalk, more research required.
