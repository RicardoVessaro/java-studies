package ricardo.vessaro.injection.service;

import org.springframework.stereotype.Component;

@Component
public class TennisCoach implements Coach {
    @Override
    public CoachType type() {
        return CoachType.TENNIS;
    }

    @Override
    public void training() {
        System.out.println("20 slices...");
    }
}
