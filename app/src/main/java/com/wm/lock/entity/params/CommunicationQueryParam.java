package com.wm.lock.entity.params;

import com.wm.lock.entity.CommunicationTerminal;
import com.wm.lock.entity.CommunicationType;

public class CommunicationQueryParam extends PageParam {

    /** 发起方 */
    private CommunicationTerminal from;

    /** 类型 */
    private CommunicationType type;

    public CommunicationTerminal getFrom() {
        return from;
    }

    public void setFrom(CommunicationTerminal from) {
        this.from = from;
    }

    public CommunicationType getType() {
        return type;
    }

    public void setType(CommunicationType type) {
        this.type = type;
    }

}
