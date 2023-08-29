package ricardo.vessaro.injection.service;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoachFactory {

    private final List<Coach> coaches;

    public CoachFactory(List<Coach> coaches) {
        this.coaches = coaches;
    }

    public Coach create(CoachType coachType) {
        for (Coach coach : coaches) {
            if(coach.type().equals(coachType)) {
                return coach;
            }
        }

        throw new RuntimeException("Coach with type " + coachType + " not found.");
    }

}
