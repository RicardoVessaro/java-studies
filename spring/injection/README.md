# Spring Injection


## Objective
Inject more than one implementation in a Spring project.

### Ideas
Try to use a list in the constructor or setter injection. Ex: Coach → [TennisCoach, CricketCoach], then return both and 
select one based on its type.