package p5_GravitationalVoronoiGame.strategy;

import java.util.List;
import java.util.Random;

import p5_GravitationalVoronoiGame.AbsPlayer;
import p5_GravitationalVoronoiGame.Board;
import p5_GravitationalVoronoiGame.Move;
import p5_GravitationalVoronoiGame.Strategy;
import p5_GravitationalVoronoiGame.MoveTimer;

// V2: use all previous moves from other player, and get position having the best score
// V3: add randomly choose positions and use the best move 
public class OnePixelToMaxScoreStrategyV3 implements Strategy{

	int[][] directions = {{-1, 0}, {-1,-1}, {0,-1}, {1,-1}, {1,0}, {1,1}, {0, 1}, {-1, 1}};
	Random r = new Random();
	Move bestMove = null;
	double maxScore = 0;
	
	@Override
	public Move makeAMove(Board board) {
		if(board.getPrevMoves().size() == 0){
			return Move.createMyMove(board.getBoardSize()/2, board.getBoardSize()/2);
		}

		bestMove = null;
		maxScore = 0;
		getBestOnePixelOffMove(board);
		getBestRandomMove(board);
		return bestMove;
	}
	
	private Move getBestOnePixelOffMove(Board board){
		List<Move> otherPlayersMove = board.getPrevMovesByPlayerId(1);
		
		for(int j=0; j<otherPlayersMove.size(); j++){
			Move prevMove = otherPlayersMove.get(j);
			for(int i=0; i<directions.length; i++){
				int nextX = prevMove.x + directions[i][0];
				int nextY = prevMove.y + directions[i][1];
				if(isValidMove(board, nextX, nextY)){
					Move thisMove = Move.createMyMove(nextX, nextY);
					double thisScore = board.testMyScoreWithThisMove(thisMove);
					if(thisScore > maxScore){
						maxScore = thisScore;
						bestMove = thisMove;
					}
				}else{
					int nextX2 = prevMove.x + directions[i][0]*2;
					int nextY2 = prevMove.y + directions[i][1]*2;
					if(isValidMove(board, nextX2, nextY2)){
						Move thisMove = Move.createMyMove(nextX2, nextY2);
						double thisScore = board.testMyScoreWithThisMove(thisMove);
						if(thisScore > maxScore){
							maxScore = thisScore;
							bestMove = thisMove;
						}
					}
				}
			}
		}
		return bestMove;
	}
	
	private Move getBestRandomMove(Board board){
		int poolNo = 1000;
		int[][] currColor = board.getCurrColor();
		
		for(int i=0; i<poolNo; i++){
			Move nextMove = null;
			while(nextMove == null){
				int x = r.nextInt(board.getBoardSize());
				int y = r.nextInt(board.getBoardSize());
				
				// if this position is empty and the color of this position is not mine, then accept this move
				if(board.isEmptyAt(x, y) && currColor[x][y] != 0){
					nextMove = Move.createMyMove(x, y);
					double thisScoreDiff = board.testMyScoreWithThisMove(nextMove);
					if(thisScoreDiff > maxScore){
						maxScore = thisScoreDiff;
						bestMove = nextMove;
					}
				}
			}
		}
		return bestMove;
	}
	
	private boolean isValidMove(Board board, int x, int y){
		if(x < 0 || y < 0 || x >= board.getBoardSize() || y >= board.getBoardSize() || !board.isEmptyAt(x, y)){
			return false;
		}
		
		return true;
	}

}
