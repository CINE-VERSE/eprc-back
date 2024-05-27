package com.cineverse.erpc.order.note.dto;

import com.cineverse.erpc.employee.aggregate.Employee;
import com.cineverse.erpc.order.order.aggregate.Order;
import lombok.Data;

@Data
public class ResponseFindOrderNotesDTO {
    private long orderNoteId;
    private String orderNote;
    private String orderNoteDate;
    private String orderDeleteDate;
    private Employee employee;
}
