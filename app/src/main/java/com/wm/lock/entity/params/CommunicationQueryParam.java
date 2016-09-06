package com.wm.lock.entity.params;

import com.wm.lock.entity.CommunicationTerminal;
import com.wm.lock.entity.CommunicationType;

public class CommunicationQueryParam extends PageParam {

    /** 发起方 */
    private CommunicationTerminal sender;

    /** 类型 */
    private CommunicationType type;

    public CommunicationTerminal getSender() {
        return sender;
    }

    public void setSender(CommunicationTerminal sender) {
        this.sender = sender;
    }

    public CommunicationType getType() {
        return type;
    }

    public void setType(CommunicationType type) {
        this.type = type;
    }

}
