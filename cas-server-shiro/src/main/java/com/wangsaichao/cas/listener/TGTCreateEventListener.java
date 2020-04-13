/*
 * 版权所有.(c)2008-2017. 卡尔科技工作室
 */

package com.wangsaichao.cas.listener;

import org.apereo.cas.support.events.ticket.CasTicketGrantingTicketCreatedEvent;
import org.apereo.cas.ticket.TicketGrantingTicket;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 识别事件然后删除
 *
 * @author Carl
 * @version 创建时间：2017/11/29
 */
public class TGTCreateEventListener {

    private TriggerLogoutService logoutService;
    private IUserIdObtainService service;

    public TGTCreateEventListener(@NotNull TriggerLogoutService logoutService, @NotNull IUserIdObtainService service) {
        this.logoutService = logoutService;
        this.service = service;
    }

    @EventListener
    @Async
    public void onTgtCreateEvent(CasTicketGrantingTicketCreatedEvent event) {
        TicketGrantingTicket ticketGrantingTicket = event.getTicketGrantingTicket();
        String id = ticketGrantingTicket.getAuthentication().getPrincipal().getId();
        String tgt = ticketGrantingTicket.getId();
        String clientName = (String) ticketGrantingTicket.getAuthentication().getAttributes().get("clientName");
        System.out.println(" ---------------- ");
        System.out.println("id : " +id);
        System.out.println("tgt : "+tgt);
        System.out.println("clientName : "+clientName);
        System.out.println(" ---------------- ");
        //获取可以认证的id
        // ---- 此处下面弄下来 ----
        List<String> authIds = service.obtain(clientName, id);
        if (authIds != null) {
            //循环触发登出
            authIds.forEach(authId -> logoutService.triggerLogout(authId, tgt));
        }
    }
}
