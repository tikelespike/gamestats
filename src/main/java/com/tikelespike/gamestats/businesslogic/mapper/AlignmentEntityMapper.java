package com.tikelespike.gamestats.businesslogic.mapper;

import com.tikelespike.gamestats.businesslogic.entities.Alignment;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.AlignmentEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between the alignment business object and the alignment entity database representation.
 */
@Component
public class AlignmentEntityMapper extends Mapper<Alignment, AlignmentEntity> {

    @Override
    protected Alignment toBusinessObjectNoCheck(AlignmentEntity transferObject) {
        return Alignment.valueOf(transferObject.name());
    }

    @Override
    protected AlignmentEntity toTransferObjectNoCheck(Alignment businessObject) {
        return AlignmentEntity.valueOf(businessObject.name());
    }
}
