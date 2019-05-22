#!/bin/sh

lscard=$?
monutcard=$?
get_mount_app()
{
	lscard=`ls /tmp/fuse_d | grep DCIM`
}

get_mount_app

if [ "${lscard}" == "DCIM" ]; then
	monutcard=`mount | grep /var/www/DCIM` 
	if [ "${monutcard}" == "" ]; then
		mount --bind /tmp/fuse_d/DCIM/ /var/www/DCIM
	fi
fi


