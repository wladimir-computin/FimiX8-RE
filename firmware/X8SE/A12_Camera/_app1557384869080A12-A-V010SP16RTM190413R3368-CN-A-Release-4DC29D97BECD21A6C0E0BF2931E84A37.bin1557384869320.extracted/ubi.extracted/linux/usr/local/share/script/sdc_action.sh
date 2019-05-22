#!/bin/sh

if [ $# != 2 ]; then
echo "Sdc action error!"
exit 1;
fi

case "$2" in
  insert)
        mkdir -p /tmp/$1
        mount /dev/$1 /tmp/$1
        ;;
  remove)
        umount /tmp/$1
        ;;
  *)
        echo $"Sdc cmd error!"
	;;

esac
