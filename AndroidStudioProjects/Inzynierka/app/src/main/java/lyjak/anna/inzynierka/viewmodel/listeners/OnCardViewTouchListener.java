package lyjak.anna.inzynierka.viewmodel.listeners;

/**
 * Created by Anna on 20.10.2017.
 */

public interface OnCardViewTouchListener {
    void onCardMove(int fromPosition, int toPosition);
    void onCardDismiss(int position);
}
