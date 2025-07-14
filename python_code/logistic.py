import numpy as np

# 1. Sigmoid function
def sigmoid(z):
    return 1 / (1 + np.exp(-z))

# 2. Initialize weights and bias
def initialize_params(n_features):
    w = np.zeros((n_features, 1))  # column vector
    b = 0
    return w, b

# 3. Forward and backward propagation
def propagate(w, b, X, Y):
    m = X.shape[1]  # number of examples

    # Forward
    A = sigmoid(np.dot(w.T, X) + b)     # shape: (1, m)
    cost = -(1/m) * np.sum(Y * np.log(A) + (1 - Y) * np.log(1 - A))

    # Backward
    dw = (1/m) * np.dot(X, (A - Y).T)
    db = (1/m) * np.sum(A - Y)

    grads = {"dw": dw, "db": db}
    return grads, cost

# 4. Gradient descent optimization
def optimize(w, b, X, Y, learning_rate, num_iterations):
    costs = []

    for i in range(num_iterations):
        grads, cost = propagate(w, b, X, Y)
        dw = grads["dw"]
        db = grads["db"]

        w -= learning_rate * dw
        b -= learning_rate * db

        if i % 100 == 0:
            costs.append(cost)
            print(f"Iteration {i}: cost = {cost:.4f}")

    return w, b

# 5. Predict function
def predict(w, b, X):
    A = sigmoid(np.dot(w.T, X) + b)
    return A > 0.5  # returns boolean array

# 6. Training the model
def logistic_regression_model(X_train, Y_train, learning_rate=0.01, num_iterations=1000):
    n_features = X_train.shape[0]
    w, b = initialize_params(n_features)

    w, b = optimize(w, b, X_train, Y_train, learning_rate, num_iterations)

    return w, b
# Toy dataset (2 features, 4 examples)
X_train = np.array([[0, 1, 2, 3], [1, 2, 3, 4]])  # shape (2, 4)
Y_train = np.array([[0, 0, 1, 1]])               # shape (1, 4)

w, b = logistic_regression_model(X_train, Y_train, learning_rate=0.1, num_iterations=1000)

preds = predict(w, b, X_train)
print("Predictions:", preds.astype(int))
