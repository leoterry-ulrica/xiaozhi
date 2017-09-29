package com.dist.bdf.base.upload;

import org.apache.commons.fileupload.ProgressListener;

import com.dist.bdf.base.constants.SessionContants;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

public class PJProgressListener implements ProgressListener {
    private HttpSession session;

    public PJProgressListener() {
    }

    public PJProgressListener(HttpSession _session) {
        session = _session;
        ProgressEntity ps = new ProgressEntity();
        session.setAttribute(SessionContants.UPLOAD_FILE, ps);
    }

    public void update(long pBytesRead, long pContentLength, int pItems) {
        ProgressEntity ps = (ProgressEntity) session.getAttribute(SessionContants.UPLOAD_FILE);
        ps.setBytesRead(pBytesRead);
        ps.setContentLength(pContentLength);
        ps.setItems(pItems);

        //保留小数点后两位
        /*double pre = (double) pBytesRead / (double) pContentLength;
        BigDecimal b = new BigDecimal(pre);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        int percentage = (int) (f1 * 100);
        ps.setPercent(percentage);*/
        //更新
        session.setAttribute(SessionContants.UPLOAD_FILE, ps);
    }
}
