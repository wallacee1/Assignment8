package com.example.assignment8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;


public class BlackJackMainActivity extends AppCompatActivity {
    private Deck deck;
    private List<Card> dealerHand;
    private List<Card> playerHand;
    private TextView dealerScore;
    private TextView playerScore;
    private ImageView dcardOne, dcardTwo, pcardOne, pcardTwo;

    private Button hitButton;
    private Button stayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dealerScore = findViewById(R.id.dealerScore);
        playerScore = findViewById(R.id.playerScore);
        hitButton = findViewById(R.id.hitButton);
        stayButton = findViewById(R.id.stayButton);

        dcardOne = findViewById(R.id.dcardOne);
        dcardTwo = findViewById(R.id.dcardTwo);
        pcardOne = findViewById(R.id.pcardOne);
        pcardTwo = findViewById(R.id.pcardTwo);

        startNewGame();
        updateScores();

        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerHand.add(deck.drawCard());
                updatePlayerCards();
                updateScores();
                checkPlayerBust();
            }
        });


        stayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealerPlay();
                updateDealerCards();
                updateScores();
                checkWinner();
            }
        });

    }

    private void startNewGame() {
        deck = new Deck();
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();

        dealerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard());
        playerHand.add(deck.drawCard());
        playerHand.add(deck.drawCard());

        updateScores();
    }

    private void updateScores() {
        dealerScore.setText(String.valueOf(calculateScore(dealerHand)));
        playerScore.setText(String.valueOf(calculateScore(playerHand)));
        updateDealerCards();
        updatePlayerCards();
    }



    private int calculateScore(List<Card> hand) {
        int score = 0;
        int aces = 0;

        for (Card card : hand) {
            if (card.getRank() == Card.Rank.ACE) {
                aces++;
            }
            score += card.getValue();
        }

        while (score > 21 && aces > 0) {
            score -= 10;
            aces--;
        }

        return score;
    }

    private void checkPlayerBust() {
        if (calculateScore(playerHand) > 21) {
            // Player busts; handle game over
            showGameResult("Player busts! Dealer wins!");
        }
    }



    private void dealerPlay() {
        int playerTotal = calculateScore(playerHand);
        while (calculateScore(dealerHand) < 17 && calculateScore(dealerHand) <= playerTotal) {
            dealerHand.add(deck.drawCard());
        }

        // Check for dealer bust
        if (calculateScore(dealerHand) > 21) {
            // Dealer busts; handle game over
            showGameResult("Dealer busts! Player wins!");
        } else {
            // No bust, check for the winner
            checkWinner();
        }
    }




    private void updateDealerCards() {
        dcardOne.setImageResource(getCardDrawableId(dealerHand.get(0), true));
        dcardTwo.setImageResource(getCardDrawableId(dealerHand.get(1), false));
    }


    private int getCardDrawableId(Card card, boolean faceDown) {
        if (faceDown) {
            return R.drawable.robodog;
        }
        String cardName = card.getRank().toString().toLowerCase() + "_of_" + card.getSuit().toString().toLowerCase();
        return getResources().getIdentifier(cardName, "drawable", getPackageName());
    }

    private void updatePlayerCards() {
        pcardOne.setImageResource(getCardDrawableId(playerHand.get(0), false));
        pcardTwo.setImageResource(getCardDrawableId(playerHand.get(1), false));
    }

    private void checkWinner() {
        int dealerTotal = calculateScore(dealerHand);
        int playerTotal = calculateScore(playerHand);
        String resultMessage;

        if (dealerTotal > 21 || playerTotal > dealerTotal) {
            // Player wins
            resultMessage = "Player wins!";
        } else if (dealerTotal == playerTotal) {
            // Draw
            resultMessage = "It's a draw!";
        } else {
            // Dealer wins
            resultMessage = "Dealer wins!";
        }

        // Show the result message and start a new game
        showGameResult(resultMessage);
    }


    private void showGameResult(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage(message);
        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startNewGame();
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }


}
