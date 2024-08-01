import React, { useState } from "react"; // eslint-disable-line no-unused-vars
import { Card } from "react-bootstrap";

const QuizScoreCard = ({ correctCount, scorePercentage, totalQuestions }) => {
  const getFeedbackMessage = (score) => {
    if (score >= 80) {
      return "Great Job!!";
    } else if (score >= 60) {
      return "You can do better";
    } else {
      return "Oops! Try again";
    }
  };

  return (
    <Card className="quiz-score-card">
      <Card.Body className="quiz-score-card-body">
        <div className="center">
          <Card.Title>{scorePercentage}</Card.Title>
          <Card.Text>{getFeedbackMessage(scorePercentage)}</Card.Text>
          <Card.Text>
            Your Score: {correctCount}/{totalQuestions}
          </Card.Text>
        </div>
      </Card.Body>
    </Card>
  );
};

export default QuizScoreCard;
