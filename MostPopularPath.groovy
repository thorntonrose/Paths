def cli = new CliBuilder(usage: "${this.class.simpleName} <file>", header: "options:")
cli.v("verbose")
def opt = cli.parse(args)
if (! opt) { return }

def inputFileName = opt.arguments()[0]

if (! inputFileName) {
   cli.usage()
   return
}

if (opt.v) { println "load file ..." }
def lines = new File(inputFileName).text.split("\n")
def custPages = [:]

lines.eachWithIndex { line, i ->
   def parts = line.split(", ")
   def cid = parts[1]
   def pgid = parts[2]

   if (!custPages[cid]) { custPages[cid] = [] }
   custPages[cid] << pgid
}

if (opt.v) { println "calculate path counts ..." }
def pathCounts = [:]
def path = []

custPages.each { cid, pages ->
   pages.eachWithIndex { pgid, i ->
      path << pgid

      if (path.size() == 3 /*|| i == (pages.size() - 1)*/) {
         if (!pathCounts[path]) { pathCounts[path] = 0 }
         pathCounts[path] ++
         path = []
      }
   }
}

if (opt.v) {
   println "cust pages: " + custPages.sort()
   println "path counts: " + pathCounts.sort { a, b -> a.value <=> b.value }
}

println pathCounts.max { it.value }