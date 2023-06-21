package top.chenjipdc.mocks.run;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pipeline implements LifeCycle {

    private final MockService mockService;

    private final SinkService sinkService;

    public void init() {
        sinkService.init();
    }

    public void run(long size) {
        for (long i = 0; i < size; i++) {
            sinkService.sink(mockService.get());
        }
    }

    public void stop() {
        sinkService.stop();
    }

}
