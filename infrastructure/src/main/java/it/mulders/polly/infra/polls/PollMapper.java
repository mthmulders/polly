package it.mulders.polly.infra.polls;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

import it.mulders.polly.domain.polls.Option;
import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.Vote;
import it.mulders.polly.infra.votes.BallotEntity;
import it.mulders.polly.infra.votes.VoteEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA, nullValueIterableMappingStrategy = RETURN_DEFAULT)
public interface PollMapper {

    @BeanMapping(resultType = JpaBackedPoll.class)
    Poll pollEntityToPoll(PollEntity entity);

    @BeanMapping(resultType = JpaBackedOption.class)
    Option pollOptionEntityToOption(PollOptionEntity entity);

    @BeanMapping(resultType = JpaBackedBallot.class)
    Ballot ballotEntityToBallot(BallotEntity entity);

    @BeanMapping(resultType = JpaBackedVote.class)
    Vote voteEntityToVote(VoteEntity entity);

    @SubclassMapping(source = JpaBackedPoll.class, target = PollEntity.class)
    PollEntity pollToPollEntity(Poll poll);

    @Mapping(target = "poll", ignore = true)
    @SubclassMapping(source = JpaBackedBallot.class, target = BallotEntity.class)
    BallotEntity ballotToBallotEntity(Ballot ballot);

    @Mapping(target = "poll", ignore = true)
    @SubclassMapping(source = JpaBackedVote.class, target = VoteEntity.class)
    VoteEntity voteToVoteEntity(Vote vote);

    @Mapping(target = "poll", ignore = true)
    @SubclassMapping(source = JpaBackedOption.class, target = PollOptionEntity.class)
    PollOptionEntity optionToPollOptionEntity(Option option);
}
