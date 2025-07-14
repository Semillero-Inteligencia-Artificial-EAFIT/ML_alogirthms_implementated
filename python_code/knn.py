import numpy as np
from collections import Counter

class KNNClassifier:
    def __init__(self, k=3):
        self.k = k
        self.X_train = None
        self.y_train = None

    def fit(self, X, y):
        """
        Store the training data
        """
        self.X_train = X
        self.y_train = y

    def _euclidean_distance(self, x1, x2):
        """
        Compute the Euclidean distance between two vectors
        """
        return np.sqrt(np.sum((x1 - x2) ** 2))

    def predict(self, X):
        """
        Predict the class labels for input X
        """
        predictions = []

        for x_test in X:
            distances = [self._euclidean_distance(x_test, x_train) for x_train in self.X_train]
            # Get the indices of the k nearest neighbors
            k_indices = np.argsort(distances)[:self.k]
            k_nearest_labels = [self.y_train[i] for i in k_indices]
            # Vote
            most_common = Counter(k_nearest_labels).most_common(1)[0][0]
            predictions.append(most_common)

        return np.array(predictions)

# Sample training data
X_train = np.array([[1, 2], [2, 3], [3, 4], [6, 7], [7, 8]])
y_train = np.array([0, 0, 0, 1, 1])

# New test points
X_test = np.array([[2, 2], [6, 6]])

# Train and predict
knn = KNNClassifier(k=3)
knn.fit(X_train, y_train)
predictions = knn.predict(X_test)

print("Predictions:", predictions)
