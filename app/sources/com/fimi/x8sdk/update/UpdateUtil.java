package com.fimi.x8sdk.update;

import android.content.Context;
import com.fimi.host.HostConstants;
import com.fimi.host.LocalFwEntity;
import com.fimi.network.entity.UpfirewareDto;
import com.fimi.x8sdk.R;
import com.fimi.x8sdk.update.fwpack.FwInfo;
import java.util.ArrayList;
import java.util.List;

public class UpdateUtil {
    public static List<UpfirewareDto> getUpfireDtos() {
        List<UpfirewareDto> list = HostConstants.getDownZoneFinishedFw();
        List<UpfirewareDto> upfirewareDtoList = new ArrayList();
        List<LocalFwEntity> entitys = HostConstants.getLocalFwEntitys();
        if (list.size() > 0) {
            for (UpfirewareDto dto : list) {
                if (entitys.size() > 0 && ((dto.getType() == 0 && dto.getModel() == 3) || ((dto.getType() == 1 && dto.getModel() == 3) || ((dto.getType() == 9 && dto.getModel() == 1) || ((dto.getType() == 11 && dto.getModel() == 3) || ((dto.getType() == 12 && dto.getModel() == 3) || ((dto.getType() == 14 && dto.getModel() == 0) || ((dto.getType() == 3 && dto.getModel() == 6) || ((dto.getType() == 5 && dto.getModel() == 3) || ((dto.getType() == 10 && dto.getModel() == 3) || ((dto.getType() == 4 && dto.getModel() == 2) || (dto.getType() == 13 && dto.getModel() == 1)))))))))))) {
                    for (LocalFwEntity localFwEntity : entitys) {
                        if (((localFwEntity.getType() == 0 && localFwEntity.getModel() == 3) || ((localFwEntity.getType() == 1 && localFwEntity.getModel() == 3) || ((localFwEntity.getType() == 9 && localFwEntity.getModel() == 1) || ((localFwEntity.getType() == 11 && localFwEntity.getModel() == 3) || ((localFwEntity.getType() == 12 && localFwEntity.getModel() == 3) || ((localFwEntity.getType() == 14 && localFwEntity.getModel() == 0) || ((localFwEntity.getType() == 3 && localFwEntity.getModel() == 6) || ((localFwEntity.getType() == 5 && localFwEntity.getModel() == 3) || ((localFwEntity.getType() == 10 && localFwEntity.getModel() == 3) || ((localFwEntity.getType() == 4 && localFwEntity.getModel() == 2) || (localFwEntity.getType() == 13 && localFwEntity.getModel() == 1))))))))))) && localFwEntity.getType() == dto.getType() && localFwEntity.getModel() == dto.getModel()) {
                            boolean normalUpdate = localFwEntity.getLogicVersion() < dto.getLogicVersion() && "0".equals(dto.getForceSign());
                            boolean forceUpdate = localFwEntity.getLogicVersion() < dto.getLogicVersion() && "2".equals(dto.getForceSign());
                            boolean ingoreUpdate = localFwEntity.getLogicVersion() != dto.getLogicVersion() && "1".equals(dto.getForceSign());
                            boolean isUpdateZone = dto.getEndVersion() == 0 || (localFwEntity.getLogicVersion() <= ((long) dto.getEndVersion()) && localFwEntity.getLogicVersion() >= ((long) dto.getStartVersion()));
                            if ((normalUpdate || forceUpdate || ingoreUpdate) && isUpdateZone) {
                                upfirewareDtoList.add(dto);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return upfirewareDtoList;
    }

    public static boolean isForceUpdate() {
        List<UpfirewareDto> upfirewareDtos = getUpfireDtos();
        if (upfirewareDtos.size() <= 0) {
            return false;
        }
        for (UpfirewareDto dto : upfirewareDtos) {
            if ("2".equalsIgnoreCase(dto.getForceSign())) {
                return true;
            }
        }
        return false;
    }

    public static List<FwInfo> toFwInfo() {
        List<FwInfo> fws = new ArrayList();
        for (UpfirewareDto upfirewareDto : getUpfireDtos()) {
            FwInfo fwInfo = new FwInfo();
            fwInfo.setModelId((byte) upfirewareDto.getModel());
            fwInfo.setTypeId((byte) upfirewareDto.getType());
            fwInfo.setFwType(Byte.parseByte(upfirewareDto.getForceSign()));
            fwInfo.setSysName(upfirewareDto.getSysName());
            fwInfo.setSoftwareVer((short) ((int) upfirewareDto.getLogicVersion()));
            fws.add(fwInfo);
        }
        return fws;
    }

    public static List<UpfirewareDto> getServerFwInfo() {
        return HostConstants.getFirmwareDetail();
    }

    public static int getErrorCodeString(Context context, int result) {
        switch ((byte) result) {
            case (byte) -1:
                return R.string.x8_error_code_update_255;
            case (byte) 0:
                return R.string.x8_error_code_update_0;
            case (byte) 1:
                return R.string.x8_error_code_update_1;
            case (byte) 2:
                return R.string.x8_error_code_update_2;
            case (byte) 3:
                return R.string.x8_error_code_update_3;
            case (byte) 4:
                return R.string.x8_error_code_update_4;
            case (byte) 5:
                return R.string.x8_error_code_update_5;
            case (byte) 6:
                return R.string.x8_error_code_update_6;
            case (byte) 7:
                return R.string.x8_error_code_update_7;
            case (byte) 33:
                return R.string.x8_error_code_update_21;
            case (byte) 34:
                return R.string.x8_error_code_update_22;
            case (byte) 35:
                return R.string.x8_error_code_update_23;
            case (byte) 36:
                return R.string.x8_error_code_update_24;
            case (byte) 37:
                return R.string.x8_error_code_update_25;
            case (byte) 38:
                return R.string.x8_error_code_update_26;
            case (byte) 39:
                return R.string.x8_error_code_update_27;
            case (byte) 40:
                return R.string.x8_error_code_update_28;
            case (byte) 41:
                return R.string.x8_error_code_update_29;
            default:
                return 0;
        }
    }
}
