package it.mulders.polly.infra.polls;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

import it.mulders.polly.domain.polls.Poll;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA, nullValueIterableMappingStrategy = RETURN_DEFAULT)
public interface PollMapper {

    Poll pollEntityToPoll(PollEntity entity);

    PollEntity pollToPollEntity(Poll poll);
}
