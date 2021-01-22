package interfaces;

import messaging.Mark;
import models.User;

public interface IGameController {
    void connectPlayer(User player);
    String markTile(Mark mark);
}
