

var linkSource = linkSourceRepository.findById(0).or(() -> {
            var tmp = new LinkSource();
            tmp.setType(SourceType.JSOUP);
            tmp.setName("alexa_topsites");
            tmp.setDescription("Top sites from - www.alexa.com/topsites");
            linkSourceRepository.save(tmp);
            return linkSourceRepository.findById(0);
        }