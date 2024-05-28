module.exports = {
  moduleDirectories: ['node_modules', 'src'],
  moduleFileExtensions: ['js', 'json', 'ts'],
  roots: ['<rootDir>'],
  testRegex: '.test.ts$',
  transform: {
    '^.+\\.(t|j)s$': 'ts-jest',
  },
  coverageDirectory: '../../coverage',
  testEnvironment: 'node',
  collectCoverageFrom: ['src/controller/*.(t|j)s', 'src/service/*.(t|j)s', 'src/util/*.(t|j)s', '!src/**/index.(t|j)s'],
  moduleNameMapper: {
    'src/(.*)': '<rootDir>/src/$1',
  },
  testPathIgnorePatterns: ['/node_modules/', '/dist/'],
};
