package com.tikelespike.gamestats.common;

/**
 * Maps between business objects of a certain type and a transfer representation of that type. The transfer
 * representation should be understood in the broader term of a representation used in another layer or context.
 *
 * @param <BusinessType> the business object type
 * @param <TransferType> the transfer object type
 */
public abstract class Mapper<BusinessType, TransferType> {

    /**
     * Maps from a transfer object to a business object. You do not need to check that the transfer object is null. It
     * is assumed by contract to be non-null.
     *
     * @param transferObject the transfer object to map. Is not null.
     *
     * @return a corresponding business object
     */
    protected abstract BusinessType toBusinessObjectNoCheck(TransferType transferObject);

    /**
     * Maps from a business object to a transfer object. You do not need to check that the business object is null. It
     * is assumed by contract to be non-null.
     *
     * @param businessObject the business object to map. Is not null.
     *
     * @return a corresponding transfer object
     */
    protected abstract TransferType toTransferObjectNoCheck(BusinessType businessObject);

    /**
     * Maps from a transfer object to a business object.
     *
     * @param transferObject the transfer object to map. May be null.
     *
     * @return a corresponding business object, or null if the transfer object is null
     */
    public BusinessType toBusinessObject(TransferType transferObject) {
        if (transferObject == null) {
            return null;
        }
        return toBusinessObjectNoCheck(transferObject);
    }

    /**
     * Maps from a business object to a transfer object.
     *
     * @param businessObject the business object to map. May be null.
     *
     * @return a corresponding transfer object, or null if the business object is null
     */
    public TransferType toTransferObject(BusinessType businessObject) {
        if (businessObject == null) {
            return null;
        }
        return toTransferObjectNoCheck(businessObject);
    }
}
