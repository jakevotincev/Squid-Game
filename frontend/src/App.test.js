import { render, screen } from '@testing-library/react';
import Glavniy from './static/pages/Glavniy';

test('renders learn react link', () => {
  render(<Glavniy />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
