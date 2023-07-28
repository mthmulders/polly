package it.mulders.polly.infra.votes;

import it.mulders.polly.domain.votes.Ballot;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface BallotMapper {
    Ballot ballotEntityToBallot(BallotEntity entity);

    BallotEntity ballotToBallotEntity(Ballot ballot);
}
