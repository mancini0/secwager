module.exports = {
  mode: 'development',
            module: {
                rules: [
                    {
                        loader: 'babel-loader',
                        test: /\.js$/,
                        exclude: /node_modules/
                    }, {
                        test: /\.css$/,
                        use: ['style-loader', 'css-loader']
                    }
                ]
            }
};