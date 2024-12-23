package com.tikelespike.gamestats.common;

/**
 * Maps between business objects of a certain type and a transfer representation of that type. The transfer
 * representation should be understood in the broader term of a representation used in another layer or context.
 *
 * @param <BusinessType> the business object type
 * @param <TransferType> the transfer object type
 */
public interface Mapper<BusinessType, TransferType> {

    /**
     * Maps from a transfer object to a business object.
     *
     * @param transferObject the transfer object to map
     *
     * @return a corresponding business object
     */
    BusinessType toBusinessObject(TransferType transferObject);

    /**
     * Maps from a business object to a transfer object.
     *
     * @param businessObject the business object to map
     *
     * @return a corresponding transfer object
     */
    TransferType toTransferObject(BusinessType businessObject);
}
