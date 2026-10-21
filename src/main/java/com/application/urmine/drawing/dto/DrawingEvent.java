package com.application.urmine.drawing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawingEvent {

//    private String roomId;
//
//    private String userId;
//
//    private DrawingEventType type;
//
//    private DrawingPoint point;

    private String type;

    /*
     * Identifies the client that produced the event.
     * The sender uses it to ignore the echo of its own
     * stroke.
     */

    private String clientId;

    /*
     * A full stroke, collected on the client while
     * the mouse was held down and sent as one
     * message when it was released.
     */

    private List<DrawingPoint> points;

    /*
     * Groups the chunks of a single stroke. A long drag is
     * split into several frames to stay under the WebSocket
     * message size limit, and these fields let a receiver
     * recognise the chunks as one continuous stroke.
     */

    private String strokeId;

    private int chunkIndex;

    private int chunkCount;
}
