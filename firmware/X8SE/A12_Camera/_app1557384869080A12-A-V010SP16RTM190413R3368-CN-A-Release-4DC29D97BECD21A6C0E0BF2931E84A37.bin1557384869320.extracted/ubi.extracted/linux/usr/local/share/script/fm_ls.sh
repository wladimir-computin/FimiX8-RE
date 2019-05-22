#!/bin/sh
c_dcf_scan_dcim_media_main ()
{
	sed_p=p
	saloar=/
	scan_dir=/tmp/fuse_d/DCIM
			
	#pref_dir=/pref
	pref_dir=/var/www/mjpeg

	#echo `date +%Y-%m-%d_%H:%M:%S`
	#echo " START scan_dcim_media"
			
	if [ -f $pref_dir/media.xml ]; then
	chmod 777 $pref_dir/media.xml
	rm -rf $pref_dir/media.xml
	fi
			
	ls -F $scan_dir | grep -E "1+[0-9]{2}/$" > $pref_dir/subdir_tmp.lst
	tac $pref_dir/subdir_tmp.lst > $pref_dir/subdir.lst
	k=1
	FILE=$pref_dir/subdir.lst
	while read line; do
		cd $scan_dir$saloar$line
		ls -lget | grep -E "IMG+_+20+[1-9]{2}+[0-9]{4}+_+[0-9]{6}+_+[0-9]{4}.[jpg,JPG,rlv,RLV]|PIV+_+20+[1-9]{2}+[0-9]{4}+_+[0-9]{6}+_+[0-9]{4}.[jpg,JPG,rlv,RLV]|PAN+_+20+[1-9]{2}+[0-9]{4}+_+[0-9]{6}+_+[0-9]{4}.[rlv,RLV,lst,LST]|VID+_+20+[1-9]{2}+[0-9]{4}+_+[0-9]{6}+_+[0-9]{4}.[MP4,mp4,thm,THM,rlv,RLV]"\
							| sed "s:$: /DCIM/$line:" | awk '{print $4"|"$9"-"$6"-"$7"_"$8"|"$10"|"$11}' >> $pref_dir/media.xml
		done < $FILE
	if [ -f $pref_dir/subdir.lst ]; then
			chmod 777 $pref_dir/subdir.lst
			rm -rf $pref_dir/subdir.lst
	fi
	
	if [ -f $pref_dir/subdir_tmp.lst ]; then
	chmod 777 $pref_dir/subdir_tmp.lst
	rm -rf $pref_dir/subdir_tmp.lst
	fi
}

c_dcf_scan_dcim_media_sec ()
{
	sed_p=p
	saloar=/
	scan_dir=/tmp/fuse_d/MISC/THM/100MEDIA
			
	#pref_dir=/pref
	pref_dir=/var/www/mjpeg

	#echo `date +%Y-%m-%d_%H:%M:%S`
	#echo " START scan_dcim_media"
			
	if [ -f $pref_dir/media.xml ]; then
	chmod 777 $pref_dir/media.xml
	rm -rf $pref_dir/media.xml
	fi
			
	ls -F $scan_dir | grep -E "MEDIA/$" > $pref_dir/subdir_tmp.lst
	tac $pref_dir/subdir_tmp.lst > $pref_dir/subdir.lst
	
	#echo "############start############################"
	k=1
	FILE=$pref_dir/subdir.lst
	while read line; do
		cd $scan_dir$saloar$line
		ls -lget | grep -E "IMG+_+20+[1-9]{2}+[0-9]{4}+_+[0-9]{6}.[jpg,JPG]|IMG+_+20+[1-9]{2}+[0-9]{4}+_+[0-9]{6}+_+[0-9]{4}.[jpg,JPG]|VID+_+20+[1-9]{2}+[0-9]{4}+_+[0-9]{6}+[_THM,_thm]{1}+[.MP4,.mp4]{1}|VID+_+20+[1-9]{2}+[0-9]{4}+_+[0-9]{6}+_+[0-9]{4}.thm|VID+_+20+[1-9]{2}+[0-9]{4}+_+[0-9]{6}+_+[0-9]{4}.THM"\
							| sed "s:$: /DCIM/$line:" | awk '{print $4"|"$9"-"$6"-"$7"_"$8"|"$10"|"$11}' >> $pref_dir/media.xml
		
		done < $FILE
	if [ -f $pref_dir/subdir.lst ]; then
			chmod 777 $pref_dir/subdir.lst
			rm -rf $pref_dir/subdir.lst
	fi
	
	if [ -f $pref_dir/subdir_tmp.lst ]; then
	chmod 777 $pref_dir/subdir_tmp.lst
	rm -rf $pref_dir/subdir_tmp.lst
	fi
}

c_dcf_scan_dcim_media_all ()
{
	sed_p=p
	saloar=/
	scan_dir=/tmp/fuse_d/DCIM
			
	#pref_dir=/pref
	pref_dir=/var/www/mjpeg

	#echo `date +%Y-%m-%d_%H:%M:%S`
	#echo " START scan_dcim_media"
			
	if [ -f $pref_dir/media.xml ]; then
	chmod 777 $pref_dir/media.xml
	rm -rf $pref_dir/media.xml
	fi
			
	ls -F $scan_dir | grep -E "MEDIA/$" > $pref_dir/subdir_tmp.lst
	tac $pref_dir/subdir_tmp.lst > $pref_dir/subdir.lst
	
	#echo "############start############################"
	k=1
	FILE=$pref_dir/subdir.lst
	while read line; do
		#echo "line $k:$line"
		cd $scan_dir$saloar$line
		ls -lget | grep -E "*.jpg|*.JPG|*.MP4|*.mp4|*.thm|*.THM"| sed "s:$: /DCIM/$line:" | awk '{print $4"|"$9"-"$6"-"$7"_"$8"|"$10"|"$11}' >> $pref_dir/media.xml
		#cd ..
		#k=$(($k+1))
		done < $FILE
	if [ -f $pref_dir/subdir.lst ]; then
			chmod 777 $pref_dir/subdir.lst
			rm -rf $pref_dir/subdir.lst
	fi
	
	if [ -f $pref_dir/subdir_tmp.lst ]; then
	chmod 777 $pref_dir/subdir_tmp.lst
	rm -rf $pref_dir/subdir_tmp.lst
	fi
	
	#echo "#############end##############################"
}

c_dcf_scan_dcim_media_pano()
{
    echo "hello c_dcf_scan_dcim_media_pano"

    param_fm_pano_htm_file="${2}"
    param_fm_pano_mode="${3}"
    param_fm_pano_img_dnum="${4}"

    #echo $param_fm_pano_htm_file
    #echo $param_fm_pano_mode
    #echo $param_fm_pano_img_dnum

	sd_dir_root=/tmp/fuse_d
	sd_dcim_dir=$sd_dir_root/DCIM
	sd_panorama_dir=DCIM/PANORAMA

	pano_img_dir=$sd_dir_root/$sd_panorama_dir/$param_fm_pano_img_dnum
	pano_img_dir_to_lst=$sd_panorama_dir/$param_fm_pano_img_dnum
	pano_lst_file=$sd_dcim_dir/$param_fm_pano_htm_file

	#check the dir is or not exist
    if [ ! -d $pano_img_dir ]; then
        echo "No such file or directory of the" "\""$pano_img_dir"\""
    fi

	#check the file is or not exist
	if [ -f $pano_lst_file ]; then
		chmod 777 $pano_lst_file
		rm -rf $pano_lst_file
	fi

    #272647105|2019-Jan-1_01:01:02|VID_20190101_010414_0013.MP4|/DCIM/100/
	#-rwxrwxrwx 1 fimi 4194304 Jan  1 01:01 PAN_20190101_010120_0001.JPG /DCIM/PANORAMA/100
	cd $pano_img_dir
	echo $param_fm_pano_mode > $pano_lst_file
	ls -lget | grep -E "PAN+_+20+[0-9]{2}+[0-9]{4}+_+[0-9]{6}+_+[0-9]{4}.[jpg,JPG]" | sed "s:$: /$pano_img_dir_to_lst/:" | awk '{print $4"|"$9"-"$6"-"$7"_"$8"|"$10"|"$11}' >> $pano_lst_file

	echo $pano_lst_file
}

#if [ "${1}" == "1" ]; then
#	c_dcf_scan_dcim_media_main
#elif [ "${1}" == "2" ]; then
#	c_dcf_scan_dcim_media_sec
#else
#	c_dcf_scan_dcim_media_main
#fi

param1="${1}"
param2="${2}"
param3="${3}"
param4="${4}"

case $param1 in
  main)
        c_dcf_scan_dcim_media_main
        ;;
  sec)
        c_dcf_scan_dcim_media_sec
        ;;
  all)
        c_dcf_scan_dcim_media_all
        ;;
  pano)
        if [ $#==4 ]
        then
            echo "Will list the panorama file to the file"
            c_dcf_scan_dcim_media_pano $param1 $param2 $param3 $param4
        else
            echo "Params is wrong...The count of params is "$#
            echo "Usage: fm_ls.sh pano [FILENAME] [MODE] [DIRECTORY]"
            echo "Example: fm_ls.sh pano 100/PAN_20190101_010119_0002.lst PANO_WFOV 101"
            #/usr/local/share/script/fm_ls.sh pano C:\DCIM\100\PAN_20190101_010154_0004.lst PANO_WFOV 101
        fi
        ;;
  *)
        c_dcf_scan_dcim_media_main
esac
