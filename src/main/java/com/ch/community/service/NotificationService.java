package com.ch.community.service;


import com.ch.community.enums.NotificationStatusEnum;
import com.ch.community.mapper.NotificationMapper;
import com.ch.community.model.Notification;
import com.ch.community.model.NotificationExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    public List<Notification> selectNoRead(Integer userId,Integer pageCount,Integer currentPage) {
        RowBounds rowBounds=new RowBounds(pageCount,currentPage);
        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notifications=notificationMapper.selectByExampleWithRowbounds(notificationExample,rowBounds);
        if (notifications.size()==0){
            return new ArrayList<>();
        }
        return notifications;
    }
}
