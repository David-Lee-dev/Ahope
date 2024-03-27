import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import './globals.css';
import { DarkThemeToggle } from 'flowbite-react';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: 'ToolBox',
  description: 'We provide a variety of tools for free that you can use on our website',
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang='en'>
      <body className={inter.className}>{children}</body>
    </html>
  );
}
