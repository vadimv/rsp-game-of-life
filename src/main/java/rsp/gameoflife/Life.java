package rsp.gameoflife;

import rsp.App;
import rsp.Component;
import rsp.jetty.JettyServer;
import rsp.server.StaticResources;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static rsp.dsl.Html.*;

public class Life {

    public static void main(String[] args) throws Exception {
        final Map<String, ScheduledFuture<?>> timers = new ConcurrentHashMap<>();
        final Component<State> component = useState ->
                html(head(link(attr("rel", "stylesheet"), attr("href","/res/style.css"))),
                     body(div(attr("class", "tetris-wrapper"),
                             div(attr("class", "board"),
                                     of(Arrays.stream(useState.get().board.cells).flatMap(row ->

                                             Arrays.stream(row).map(cell ->
                                                     div(attr("class", "cell t" + (cell.active ? "J" : "0")),
                                                         on("click", c -> {
                                                            System.out.println("Clicked x=" + cell.x + " y=" + cell.y);
                                                            useState.accept(useState.get().toggleCell(cell.x, cell.y));
                                                     }))))))),
                             div(attr("class", "controls"),
                                     button(attr("id", "start-btn"), attr("type", "button"),
                                             when(false, () -> attr("disabled")),
                                             text("Start"), on("click", c -> {
                                                     System.out.println("Start");

                                                 //timers.put(c.sessionId().sessionId,
                                                         c.scheduleAtFixedRate(() -> useState.accept(s -> s.advance()),
                                                                          0, 200, TimeUnit.MILLISECONDS);
                                                 //);

                                             })))));
        final var initialState = State.initialState();
        final var s = new JettyServer(8080,
                "",
                new App(initialState,
                        component),
                new StaticResources(new File("src/main/java/rsp/examples/life"),
                        "/res/*"));
        s.start();
        s.join();
    }
}
