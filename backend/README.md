
Packets I got from sniffing the app traffic:
`` mitmproxy -r packets.mitm ``

The current firmware files are listed by the bachend API here:
https://fimiapp-server-frankfurt.mi-ae.com.de/v3/firmware/getFirmwareDetail

We can see, that there is more by cutting one of the downloadlink like this:
https://cdn.awsde0.fds.api.mi-img.com/fimi-firmware-bucket
```
{
   "creationTime":1525831731902,
   "enableSSE":false,
   "name":"fimi-firmware-bucket",
   "numObjects":48,
   "usedSpace":694855077
}
```

They use S3 buckets for storage, see https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketOps.html


Check permissions:
https://cdn.awsde0.fds.api.mi-img.com/fimi-firmware-bucket?acl
Yeah, everyone has access:
```
{
   "accessControlList":[
      {
         "grantee":{
            "id":"ALL_USERS"
         },
         "permission":"FULL_CONTROL",
         "type":"GROUP"
      },
      {
         "grantee":{
            "id":"101903"
         },
         "permission":"FULL_CONTROL",
         "type":"USER"
      }
   ],
   "owner":{

   }
}
```

Get all files from bucket "fimi-firmware-bucket":
https://cdn.awsde0.fds.api.mi-img.com/fimi-firmware-bucket?list-type=2&prefix=&delimiter=
(Response in getAllFirmwareNamesResponse.json)

Then I created a text file, grep'ed all the firmware names from getAllFirmwareNamesResponse.json to it and prepended the url https://cdn.awsde0.fds.api.mi-img.com/fimi-firmware-bucket/ to them. Voila, ready to download with wget.
`` wget -i downloadList.txt ``
