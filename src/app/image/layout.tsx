import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'ToolBox | Image tool',
  description: 'Change image extension and size for free',
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return <>{children}</>;
}
