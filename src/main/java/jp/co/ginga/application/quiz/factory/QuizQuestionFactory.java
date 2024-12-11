package jp.co.ginga.application.quiz.factory;

import java.util.ArrayList;
import java.util.List;

import jp.co.ginga.application.quiz.QuizQuestion;
import jp.co.ginga.util.exception.SystemException;
import jp.co.ginga.util.properties.MessageProperties;

public class QuizQuestionFactory {

	public List<QuizQuestion> createQuizQuestion() throws SystemException {
		List<QuizQuestion> list = new ArrayList<QuizQuestion>();
		try {
			int numberOfQuiz = Integer.parseInt(MessageProperties.getMessage("quiz.number.questions"));

			for (int i = 0; i < numberOfQuiz; i++) {
				String title = MessageProperties.getMessage("quiz.question.title" + (i + 1));
				String body = MessageProperties.getMessage("quiz.question.body" + (i + 1));
				String choice = MessageProperties.getMessage("quiz.question.choice" + (i + 1));
				int correct = Integer.parseInt(MessageProperties.getMessage("quiz.question.correct" + (i + 1)));
				list.add(new QuizQuestion(title, body, choice, correct));

			}
		} catch (NumberFormatException e) {
			throw new SystemException(MessageProperties.getMessage("error.properties.error"));
		}

		return list;
	}
}
