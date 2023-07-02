package it.mulders.polly.infra.polls;

import it.mulders.polly.domain.polls.Poll;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface PollMapper {

    Poll pollEntityToPoll(PollEntity entity);

    PollEntity pollToPollEntity(Poll conference);
}
