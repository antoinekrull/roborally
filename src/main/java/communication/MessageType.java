package communication;

/**
 * @author Antoine, Firas, Moritz
 * @version 1.0
 */

/**
 * Serves to categorize messages so that the server/client can process them accordingly.
 */
public enum MessageType {
    HelloClient,
    Alive,
    HelloServer,
    Welcome,
    PlayerValues,
    PlayerAdded,
    SetStatus,
    PlayerStatus,
    SelectMap,
    MapSelected,
    GameStarted,
    SendChat,
    ReceivedChat,
    Error,
    PlayCard,
    CardPlayed,
    CurrentPlayer,
    ActivePhase,
    SetStartingPoint,
    StartingPointTaken,
    YourCards,
    NotYourCards,
    ShuffleCoding,
    SelectedCard,
    CardSelected,
    SelectionFinished,
    TimerStarted,
    TimerEnded,
    CardsYouGotNow,
    CurrentCards,
    ReplaceCard,
    Movement,
    PlayerTurning,
    Animation,
    Reboot,
    RebootDirection,
    Energy,
    CheckPointReached,
    GameFinished,
    Sample,
    PlayerList, Accepted, ConnectionUpdate, UpgradeBought, BuyUpgrade, DrawDamage, Boink, CheckpointMoved, ChooseRegister, RegisterChosen, PickDamage, Goodbye
    }

