package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.AlignmentDTO;
import com.tikelespike.gamestats.businesslogic.entities.Alignment;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

/**
 * Maps between alignment business objects and their REST transfer representation.
 */
@Component
public class AlignmentMapper extends Mapper<Alignment, AlignmentDTO> {

    @Override
    protected Alignment toBusinessObjectNoCheck(AlignmentDTO transferObject) {
        return Alignment.valueOf(transferObject.name());
    }

    @Override
    protected AlignmentDTO toTransferObjectNoCheck(Alignment businessObject) {
        return AlignmentDTO.valueOf(businessObject.name());
    }
}
