package com.fimi.x8sdk.update.fwpack;

import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.x8sdk.command.FcCollection;
import java.util.ArrayList;
import java.util.List;

public class FwPackMain {
    public static void main(String[] args) {
        List<FwInfo> fws = new ArrayList();
        FwInfo f1 = new FwInfo();
        f1.setSysName(DirectoryPath.getFirmwarePath() + "/app1540867925417X1B.bin");
        f1.setModelId((byte) 1);
        f1.setTypeId((byte) 2);
        f1.setFwType((byte) 0);
        f1.setStepVer(FcCollection.MSG_SET_SURROUND_PAUSE);
        f1.setSoftwareVer((short) 100);
        fws.add(f1);
        new FirmwareBuildPack(null, fws).createUpdatePkg();
    }
}
