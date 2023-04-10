package client.utils;

import com.google.inject.Inject;

public class NetworkUtils {
    private final BoardUtils boardUtils;
    private final TaskUtils taskUtils;
    private final TaskListUtils taskListUtils;
    private final TagUtils tagUtils;
    private final SubTaskUtils subTaskUtils;
    private final ServerUtils serverUtils;

    @Inject
    public NetworkUtils(final BoardUtils boardUtils, final TaskUtils taskUtils,
                        final TaskListUtils taskListUtils, final TagUtils tagUtils,
                        final SubTaskUtils subTaskUtils, final ServerUtils serverUtils) {
        this.boardUtils = boardUtils;
        this.taskUtils = taskUtils;
        this.taskListUtils = taskListUtils;
        this.tagUtils = tagUtils;
        this.subTaskUtils = subTaskUtils;
        this.serverUtils = serverUtils;
    }

    public BoardUtils getBoardUtils() {
        return boardUtils;
    }

    public TaskUtils getTaskUtils() {
        return taskUtils;
    }

    public TaskListUtils getTaskListUtils() {
        return taskListUtils;
    }

    public TagUtils getTagUtils() {
        return tagUtils;
    }

    public SubTaskUtils getSubTaskUtils() {
        return subTaskUtils;
    }

    public ServerUtils getServerUtils() {
        return serverUtils;
    }
}
