package com.backend.profis_service_interface;

public interface ProfisOrderServiceInterface {
    String CreateOrderListRequest(Long id);

    int CreateOrderDetailRequest(long id);
}
