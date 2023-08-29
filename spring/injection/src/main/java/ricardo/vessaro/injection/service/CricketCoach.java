package ricardo.vessaro.injection.service;

import org.springframework.stereotype.Component;

@Component
public class CricketCoach implements Coach {
    @Override
    public CoachType type() {
        return CoachType.CRICKET;
    }

    @Override
    public void training() {
        System.out.println("Cricket!??");
    }
}
