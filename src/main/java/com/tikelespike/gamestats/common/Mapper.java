package com.tikelespike.gamestats.common;

public interface Mapper<BusinessType, TransferType> {
    BusinessType toBusinessObject(TransferType transferObject);

    TransferType toTransferObject(BusinessType businessObject);
}
